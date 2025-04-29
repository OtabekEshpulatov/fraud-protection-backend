package com.otabekjan.fraud_protection.view.postrequest;

import com.otabekjan.fraud_protection.entity.PostRequest;
import com.otabekjan.fraud_protection.entity.PostRequestMedia;
import com.otabekjan.fraud_protection.entity.User;
import com.otabekjan.fraud_protection.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.core.security.CurrentAuthentication;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value = "post-requests/:id", layout = MainView.class)
@ViewController(id = "PostRequest.detail")
@ViewDescriptor(path = "post-request-detail-view.xml")
@EditedEntityContainer("postRequestDc")
public class PostRequestDetailView extends StandardDetailView<PostRequest> {
    @Autowired
    private CurrentAuthentication currentAuthentication;

    @Subscribe
    public void onInitEntity(final InitEntityEvent<PostRequest> event) {
        event.getEntity().setUser((User) currentAuthentication.getUser());
    }


    @Install(to = "mediasDataGrid.createAction", subject = "initializer")
    private void mediasDataGridCreateActionInitializer(final PostRequestMedia postRequestMedia) {
        postRequestMedia.setRequest(getEditedEntity());
    }
}