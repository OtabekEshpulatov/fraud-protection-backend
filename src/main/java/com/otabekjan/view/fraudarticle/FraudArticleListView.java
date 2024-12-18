package com.otabekjan.view.fraudarticle;

import com.otabekjan.entity.FraudArticle;
import com.otabekjan.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "fraudArticles", layout = MainView.class)
@ViewController(id = "FraudArticle.list")
@ViewDescriptor(path = "fraud-article-list-view.xml")
@LookupComponent("fraudArticlesDataGrid")
@DialogMode(width = "64em")
public class FraudArticleListView extends StandardListView<FraudArticle> {
}