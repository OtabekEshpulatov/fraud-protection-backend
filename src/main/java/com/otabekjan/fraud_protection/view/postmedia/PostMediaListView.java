package com.otabekjan.fraud_protection.view.postmedia;

import com.otabekjan.fraud_protection.entity.PostMedia;
import com.otabekjan.fraud_protection.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "post-medias", layout = MainView.class)
@ViewController(id = "PostMedia.list")
@ViewDescriptor(path = "post-media-list-view.xml")
@LookupComponent("postMediasDataGrid")
@DialogMode(width = "64em")
public class PostMediaListView extends StandardListView<PostMedia> {
}