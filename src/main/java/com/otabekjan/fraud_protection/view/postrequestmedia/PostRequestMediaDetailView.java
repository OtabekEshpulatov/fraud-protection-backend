package com.otabekjan.fraud_protection.view.postrequestmedia;

import com.otabekjan.fraud_protection.entity.PostRequestMedia;
import com.otabekjan.fraud_protection.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "post-request-medias/:id", layout = MainView.class)
@ViewController(id = "PostRequestMedia.detail")
@ViewDescriptor(path = "post-request-media-detail-view.xml")
@EditedEntityContainer("postRequestMediaDc")
public class PostRequestMediaDetailView extends StandardDetailView<PostRequestMedia> {
}