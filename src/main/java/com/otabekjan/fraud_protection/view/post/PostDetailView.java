package com.otabekjan.fraud_protection.view.post;

import com.otabekjan.fraud_protection.entity.Post;
import com.otabekjan.fraud_protection.entity.PostMedia;
import com.otabekjan.fraud_protection.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "posts/:id", layout = MainView.class)
@ViewController(id = "Post.detail")
@ViewDescriptor(path = "post-detail-view.xml")
@EditedEntityContainer("postDc")
public class PostDetailView extends StandardDetailView<Post> {

    @Install(to = "mediasDataGrid.createAction", subject = "initializer")
    private void mediasDataGridCreateActionInitializer(final PostMedia postMedia) {
        postMedia.setPost(getEditedEntity());
    }


}