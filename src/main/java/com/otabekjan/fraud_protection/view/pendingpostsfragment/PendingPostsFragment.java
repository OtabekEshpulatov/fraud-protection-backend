package com.otabekjan.fraud_protection.view.pendingpostsfragment;

import com.otabekjan.fraud_protection.$;
import com.otabekjan.fraud_protection.entity.PostRequest;
import com.otabekjan.fraud_protection.view.postrequest.PostRequestApprovalView;
import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.grid.ItemDoubleClickEvent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import io.jmix.core.DataManager;
import io.jmix.core.SaveContext;
import io.jmix.flowui.DialogWindows;
import io.jmix.flowui.Dialogs;
import io.jmix.flowui.component.grid.DataGrid;
import io.jmix.flowui.fragment.Fragment;
import io.jmix.flowui.fragment.FragmentDescriptor;
import io.jmix.flowui.kit.action.ActionPerformedEvent;
import io.jmix.flowui.kit.action.ActionVariant;
import io.jmix.flowui.kit.action.BaseAction;
import io.jmix.flowui.kit.component.button.JmixButton;
import io.jmix.flowui.model.CollectionLoader;
import io.jmix.flowui.view.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

@FragmentDescriptor("pending-posts-fragment.xml")
public class PendingPostsFragment extends Fragment<VerticalLayout> {

    private View<?> origin;
    @ViewComponent
    private CollectionLoader<PostRequest> postRequestDl;
    @Autowired
    private DialogWindows dialogWindows;
    @ViewComponent
    private DataGrid<PostRequest> postRequestsGrid;
    @Autowired
    private Dialogs dialogs;
    @Autowired
    private DataManager dataManager;


    @Subscribe(target = Target.HOST_CONTROLLER)
    public void onHostInit(final View.InitEvent event) {
        this.origin = event.getSource();
        postRequestDl.load();
    }

    @Subscribe(id = "refreshButton", subject = "clickListener")
    public void onRefreshButtonClick(final ClickEvent<JmixButton> event) {
        postRequestDl.load();
    }

    @Subscribe("postRequestsGrid")
    public void onPostRequestsGridItemDoubleClick(final ItemDoubleClickEvent<PostRequest> event) {
        dialogWindows.detail(origin, PostRequest.class).withViewClass(PostRequestApprovalView.class).editEntity(event.getItem()).withAfterCloseListener(closeEvent -> {
            if (closeEvent.closedWith(StandardOutcome.SAVE)) {
                postRequestDl.load();
            }
        }).open();
    }

    @Subscribe("postRequestsGrid.rejectAction")
    public void onPostRequestsGridRejectAction(final ActionPerformedEvent event) {
        Set<PostRequest> postRequests = $.nonNull(postRequestsGrid.getSelectedItems());
        if (!postRequests.isEmpty()) {
            dialogs.createOptionDialog().withText("Do you want to cancel all the posts?").withActions(new BaseAction("cancel").withText("Cancel").withVariant(ActionVariant.PRIMARY), new BaseAction("ok").withText("Ok").withHandler(actionPerformedEvent -> {
                SaveContext saveContext = new SaveContext();
                for (PostRequest postRequest : postRequests) {
                    saveContext.removing(postRequest);
                }
                dataManager.save(saveContext);
                postRequestDl.load();
            }).withVariant(ActionVariant.DANGER)).build().open();
        }


    }


}