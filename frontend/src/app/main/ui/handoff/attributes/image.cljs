;; This Source Code Form is subject to the terms of the Mozilla Public
;; License, v. 2.0. If a copy of the MPL was not distributed with this
;; file, You can obtain one at http://mozilla.org/MPL/2.0/.
;;
;; Copyright (c) UXBOX Labs SL

(ns app.main.ui.handoff.attributes.image
  (:require
   [rumext.alpha :as mf]
   [cuerdas.core :as str]
   [app.config :as cfg]
   [app.util.i18n :refer [t]]
   [app.util.dom :as dom]
   [app.main.ui.icons :as i]
   [app.util.code-gen :as cg]
   [app.main.ui.components.copy-button :refer [copy-button]]))

(defn has-image? [shape]
  (and (= (:type shape) :image)))

(mf/defc image-panel [{:keys [shapes locale]}]
  (let [shapes (->> shapes (filter has-image?))]
    (for [shape shapes]
      [:div.attributes-block {:key (str "image-" (:id shape))}
       [:div.attributes-image-row
        [:div.attributes-image
         [:img {:src (cfg/resolve-file-media (-> shape :metadata))}]]]

       [:div.attributes-unit-row
        [:div.attributes-label (t locale "handoff.attributes.image.width")]
        [:div.attributes-value (-> shape :metadata :width) "px"]
        [:& copy-button {:data (cg/generate-css-props shape :width)}]]

       [:div.attributes-unit-row
        [:div.attributes-label (t locale "handoff.attributes.image.height")]
        [:div.attributes-value (-> shape :metadata :height) "px"]
        [:& copy-button {:data (cg/generate-css-props shape :height)}]]

       (let [mtype (-> shape :metadata :mtype)
             name (:name shape)
             extension (dom/mtype->extension mtype)]
         [:a.download-button {:target "_blank"
                              :download (if extension
                                          (str name "." extension)
                                          name)
                              :href (cfg/resolve-file-media (-> shape :metadata))}
          (t locale "handoff.attributes.image.download")])])))
