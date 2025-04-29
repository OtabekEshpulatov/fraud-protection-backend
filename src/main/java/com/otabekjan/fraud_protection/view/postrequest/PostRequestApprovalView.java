package com.otabekjan.fraud_protection.view.postrequest;

import com.otabekjan.fraud_protection.entity.PostRequest;
import com.otabekjan.fraud_protection.service.PostService;
import com.otabekjan.fraud_protection.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.component.textfield.TypedTextField;
import io.jmix.flowui.view.*;
import lombok.RequiredArgsConstructor;

@Route(value = "post-request-approval/:id", layout = MainView.class)
@ViewController(id = "PostRequest_approval_view")
@ViewDescriptor(path = "post-request-approval-view.xml")
@EditedEntityContainer("postRequestDc")
@RequiredArgsConstructor
public class PostRequestApprovalView extends StandardDetailView<PostRequest> {

    private final PostService postService;

    @ViewComponent
    private TypedTextField<Object> tags;

    @Subscribe
    public void onBeforeClose(final BeforeCloseEvent event) {
        postService.approvePost(getEditedEntity(), tags.getValue());
    }


}