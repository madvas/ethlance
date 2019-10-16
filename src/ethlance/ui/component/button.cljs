(ns ethlance.ui.component.button
  "An ethlance button component and additional child components."
  (:require
   [ethlance.ui.component.icon :refer [c-icon]]))


(defn c-button
  "Ethlance Button Component.
  
  # Keyword Arguments

  props - A keyword map of additional properties. Also accepts React DOM Properties.

  children - React Components making up the button component.

  # Custom Properties
  
  :disabled? - If true, button is placed in the 'disabled' visual
  state [default: false]

  :active? - If true, places button in 'active' visual state. For use
  with several button listings. [default: false]

  :color - Color scheme used with the given button. Can be `:primary`,
  `:secondary`. [default: :primary].

  :size - Size dimensions of the button. Can be `:small`, `:normal`,
  `:large`, `:auto`. [default: `:normal`]

  "
  []
  (fn [{:keys [disabled? active? color size] :as props
        :or {color :primary disabled? false active? false size :normal}}
       & children]
    (let [props (dissoc props :disabled? :active? :color :size)]
      [:div.button
       (merge
        {:class [(when (= color :secondary) " secondary ")
                 (when disabled? " disabled ")
                 (when active? " active ")
                 (condp = size
                   :small " small "
                   :normal " "
                   :large " large "
                   :auto " auto ")]}
        props)
       children])))


(defn c-button-label
  "Button Child Component used within the `c-button` component

  # Keyword Arguments
  
  props - React DOM Properties

  children - Child Components that make up button label

  # Example

  [c-button {} [c-button-label \"Here is a label\"]]
  "
  []
  (fn [props & children]
    [:div.button-label props children]))


(defn c-button-icon-label
  "Button Child Component for displaying an Icon and a Label.

   # Keyword Arguments:
   
   opts - Optional Arguments

   # Optional Arguments

   :icon-name - Keyword name of the icon as listed for the `c-icon` component.

   :label-test - Label represented as a string.

   # Notes

   - The list of icons can be found in the ethlance.ui.component.icon namespace."
  []
  (fn [{:keys [icon-name label-text] :as opts}]
    [:div.button-icon-label
     [:div.icon
      [c-icon {:name icon-name}]]
     [:span.label label-text]]))