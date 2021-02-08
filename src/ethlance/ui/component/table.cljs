(ns ethlance.ui.component.table)

(defn- unwrap-rows
  "If rows is a collection that only contains another collection, return the inner.
  This helps make c-table interface more flexible. E.g. by allowing passing in rows from (map ...),
  that is single object instead of separate arguments"
  [rows]
  (if (and (seqable? rows) (= 1 (count rows)) (seqable? (first rows)))
    (first rows)
    rows))

(defn c-table
  "Ethlance Table Component.

  This Component simplifies table construction.

  # Keyword Arguments and Optional Arguments

  :headers - Is a collection of DOM Elements (not limited to strings)
  which constitutes the <th> set of data elements. This number of
  elements that make up the header determines the number of elements
  that must be contained within each row.

  & rows - Each row has the same number of elements as the `:header` opt-arg.

  # Examples

  [c-table
   {:headers [\"Candidate\" \"Rate\" \"Created\" \"Status\"]}

   ;; Row 0
   [[:span \"Cyrus Keegan\"]
    [:span \"$25\"]
    [:span \"5 Days Ago\"]
    [:span \"Pending\"]]

   ;; Row 1
   [[:span \"Cyrus Keegan\"]
    [:span \"$25\"]
    [:span \"5 Days Ago\"]
    [:span \"Pending\"]]]
  "
  [{:keys [headers]} & rows]
  [:div.ethlance-table
   [:table
    [:tbody
     [:tr
      (doall
       (for [[i header] (map-indexed vector headers)]
         ^{:key (str "header-" i)}
         [:th header]))]
     (doall
      (for [[i row] (map-indexed vector (unwrap-rows rows))]
        ^{:key (str "row-" i)}
        [:tr
         (for [[i elem] (map-indexed vector row)]
           ^{:key (str "elem-" i)}
           [:td elem])]))]]])
