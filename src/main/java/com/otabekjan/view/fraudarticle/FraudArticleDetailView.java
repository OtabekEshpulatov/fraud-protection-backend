package com.otabekjan.view.fraudarticle;

import com.flowingcode.vaadin.addons.markdown.MarkdownEditor;
import com.otabekjan.entity.FraudArticle;
import com.otabekjan.view.main.MainView;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;

@Route(value = "fraudArticles/:id", layout = MainView.class)
@ViewController(id = "FraudArticle.detail")
@ViewDescriptor(path = "fraud-article-detail-view.xml")
@EditedEntityContainer("fraudArticleDc")
public class FraudArticleDetailView extends StandardDetailView<FraudArticle> {

    @ViewComponent
    private VerticalLayout markdownBodyWrapper;

    private MarkdownEditor markdownBodyEditor;
    @Subscribe
    public void onBeforeShow(final BeforeShowEvent event) {
         markdownBodyEditor = new MarkdownEditor(getEditedEntity().getMarkdownBody());
        markdownBodyWrapper.add(markdownBodyEditor);
    }

    
    @Subscribe
    public void onBeforeSave(final BeforeSaveEvent event) {
        getEditedEntity().setMarkdownBody(markdownBodyEditor.getContent());
    }

}