package com.otabekjan.fraud_protection.view.postrequest;

import com.otabekjan.fraud_protection.entity.PostRequest;
import com.otabekjan.fraud_protection.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "post-requests", layout = MainView.class)
@ViewController(id = "PostRequest.list")
@ViewDescriptor(path = "post-request-list-view.xml")
@LookupComponent("postRequestsDataGrid")
@DialogMode(width = "64em")
public class PostRequestListView extends StandardListView<PostRequest> {

}