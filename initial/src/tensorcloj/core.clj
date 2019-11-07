(ns tensorcloj.core
  ""
  (:require [libpython-clj.python
             :refer [import-module
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
(defonce keras-layers (import-module "keras.layers"))
(defonce keras-models (import-module "keras.models"))
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


(def+ [[train-images , train-labels], [test-images, test-labels]] (vec mnist-data))



(def class-name  ["T-shirt/top", "Trouser", "Pullover", "Dress", "Coat"
                  "Sandal", "Shirt", "Sneaker", "Bag", "Ankle boot"])

(def train-images (/ train-images  255.0))
(def test-images (/ test-images  255.0))
(py/get-attr train-images "shape")




(defn sequential-model
  []
  (call-attr keras-models "Sequential"))

; (def model (py/call-attr-kw keras "Sequential" 
;                             [(py/call-attr-kw keras-layers "Sequential" [28,28])
;                             (py/call-attr-kw keras-layers "Dense" [128]  {"activation" "relu"})
;                             (py/call-attr-kw keras-layers "Dense" [10] {"activation" "softmax"})]
;                             ))



(defn -main
  "I don't do a whole lot ... yet."
  [& args] 
  (println "Hello, TensoreFlow tutorial"))  








