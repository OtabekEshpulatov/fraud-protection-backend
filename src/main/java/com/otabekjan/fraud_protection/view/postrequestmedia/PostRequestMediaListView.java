package com.otabekjan.fraud_protection.view.postrequestmedia;

import com.otabekjan.fraud_protection.entity.PostRequestMedia;
import com.otabekjan.fraud_protection.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "post-request-medias", layout = MainView.class)
@ViewController(id = "PostRequestMedia.list")
@ViewDescriptor(path = "post-request-media-list-view.xml")
@LookupComponent("postRequestMediasDataGrid")
@DialogMode(width = "64em")
public class PostRequestMediaListView extends StandardListView<PostRequestMedia> {
}