package com.otabekjan.fraud_protection.view.region;

import com.otabekjan.fraud_protection.entity.Region;
import com.otabekjan.fraud_protection.view.main.MainView;
import com.vaadin.flow.router.Route;
import io.jmix.flowui.view.*;


@Route(value = "regions", layout = MainView.class)
@ViewController(id = "Region.list")
@ViewDescriptor(path = "region-list-view.xml")
@LookupComponent("regionsDataGrid")
@DialogMode(width = "64em")
public class RegionListView extends StandardListView<Region> {
}