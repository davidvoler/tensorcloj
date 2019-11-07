(ns tensorcloj.core-draft
  ""
  (:require [libpython-clj.python
             :refer [import-module
                     ->py-tuple
                     get-item
                     get-attr
                     python-type
                     call-attr
                     call-attr-kw
                     att-type-map
                     ->py-dict]
             :as py]
            [clojure.pprint :as pp]))

(alter-var-root #'libpython-clj.jna.base/*python-library* (constantly "/Library/Frameworks/Python.framework/Versions/3.6/lib/python3.6/config-3.6m-darwin/libpython3.6m.dylib"))

(py/initialize!)

; (defonce ftu (import-module "__future__"))
;TensorFlow and tf.keras
(defonce tf (import-module "tensorflow"))
(defonce keras (import-module "keras"))
;Helper libraries
(defonce np (import-module "numpy"))
; requires special attentions - see libpython-clj/examples/src/matplotlib
; (defonce plt (import-module " matplotlib.pyplot"))



(defonce fashionmnist (import-module "keras.datasets.fashion_mnist"))
(def mnist-data (py/call-attr fashionmnist "load_data"))

(defmacro def+
  "binding => binding-form
  internalizes binding-forms as if by def."
  {:added "1.9", :special-form true, :forms '[(def+ [bindings*])]}
  [& bindings]
  (let [bings (partition 2 (destructure bindings))]
    (sequence cat
              ['(do)
               (map (fn [[var value]] `(def ~var ~value)) bings)
               [(mapv (fn [[var _]] (str var)) bings)]])))


; (def vec-mnist-data (vec mnist-data))
(def+ [[train-images , train-labels], [test-images, test-labels]] (vec mnist-data))
; (defonce ds (get-attr  keras "datasets.fashion_mnist"))
; (defonce train-test (call-attr fashion_mnist "loaddata"))


; model = keras.Sequential ([keras.layers.Flatten (input_shape= (28, 28))
;                            keras.layers.Dense (128, activation='relu')
;                            keras.layers.Dense (10, activation='softmax')])

(def model (py/call-attr  keras "Sequential" [
                           (py/call-attr-kw keras-layers "Flatten" [] {:input_shape   [28,28]})
                           (py/call-attr-kw keras-layers "Dense" [128] {:activation  "relu"})
                           (py/call-attr-kw keras-layers "Dense" [10] {:activation "softmax" })]))






; model.compile (optimizer='adam'
;                loss='sparse_categorical_crossentropy'
;                metrics= ['accuracy'])


(py/call-attr-kw  model "compile" [] {:optimizer "adam"
               :loss "sparse_categorical_crossentropy"
               :metrics (py/->py-list["accuracy"])})


; model.fit (train_images, train_labels, epochs=10)
(py/call-attr-kw model "fit" [train-images, train-labels ] {:epochs 10})

; test_loss, test_acc = model.evaluate (test_images,  test_labels, verbose=2)
(def+ [test-loss test-acc]  (py/call-attr-kw model "evaluate" [test-images, test-labels ] {:verbose 2} ) )

; predictions = model.predict (test_images)
(def predictions (py/call-attr model "predict" test-images))

; np.argmax (predictions [0])
(py/call-attr np "argmax" (first predictions))

;test_labels [0]
(first test-labels)


; (count mnist-data)

; (map count mnist-data)

; (let [[[a b] [c d]] mnist-data]
;   a)

; (def train-images (first (first mnist-data)))

; (def train-images (-> mnist-data first first))

; (macroexpand '(-> a first first))

; (def train-images
;   (let [[[v _] [_ _]] mnist-data]
;     v))
; (def train-labels
;   (let [[[_ v] [_ _]] mnist-data]
;     v))
; (def test-images
;   (let [[[_ _] [v _]] mnist-data]
;     v))
; (def test-labels
;   (let [[[_ _] [_ v]] mnist-data]
;     v))

; (def+ [train-images , train-labels], [test-images, test-labels]] (vec mnist-data) )






; (train-labels)
(py/get-attr train-images "shape")
; (py/call-attr-kw "len" train-labels)

; (def py/call-attr plt "figure") 

(defn -main
  "I don't do a whole lot ... yet."
  [& args] 
  (println "Hello, TensoreFlow tutorial"))  








