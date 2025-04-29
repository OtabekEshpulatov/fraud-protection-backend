package com.otabekjan.fraud_protection.view.postmedia;

import com.otabekjan.fraud_protection.entity.PostMedia;
import com.otabekjan.fraud_protection.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "post-medias/:id", layout = MainView.class)
@ViewController(id = "PostMedia.detail")
@ViewDescriptor(path = "post-media-detail-view.xml")
@EditedEntityContainer("postMediaDc")
public class PostMediaDetailView extends StandardDetailView<PostMedia> {
}