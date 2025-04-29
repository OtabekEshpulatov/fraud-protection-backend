package com.otabekjan.fraud_protection.view.region;

import com.otabekjan.fraud_protection.entity.Region;
import com.otabekjan.fraud_protection.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.EditedEntityContainer;
import io.jmix.flowui.view.StandardDetailView;
import io.jmix.flowui.view.ViewController;
import io.jmix.flowui.view.ViewDescriptor;

@Route(value = "regions/:id", layout = MainView.class)
@ViewController(id = "Region.detail")
@ViewDescriptor(path = "region-detail-view.xml")
@EditedEntityContainer("regionDc")
public class RegionDetailView extends StandardDetailView<Region> {
}