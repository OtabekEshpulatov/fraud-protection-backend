package com.otabekjan.fraud_protection.view.post;

import com.otabekjan.fraud_protection.entity.Post;
import com.otabekjan.fraud_protection.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "posts", layout = MainView.class)
@ViewController(id = "Post.list")
@ViewDescriptor(path = "post-list-view.xml")
@LookupComponent("postsDataGrid")
@DialogMode(width = "64em")
public class PostListView extends StandardListView<Post> {
}