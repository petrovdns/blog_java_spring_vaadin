package com.petrovdns.vaadin.UI;

import com.petrovdns.vaadin.UI.views.navbar.MainLayout;
import com.petrovdns.vaadin.data.service.BlogService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.charts.Chart;
import com.vaadin.flow.component.charts.model.ChartType;
import com.vaadin.flow.component.charts.model.DataSeries;
import com.vaadin.flow.component.charts.model.DataSeriesItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

@Route(value = "stats", layout = MainLayout.class)
@PageTitle("Stats | Petrov BLOG")
@PermitAll
public class StatsView extends VerticalLayout {

    private final BlogService blogService;

    @Autowired
    public StatsView(BlogService blogService) {
        this.blogService = blogService;
        addClassName("dashboard-view");
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        add(
                getContactsStats(),
                getCompaniesChart()

        );
    }

    private Component getCompaniesChart() {
        Chart chart = new Chart(ChartType.PIE);

        DataSeries dataSeries = new DataSeries();
        blogService.findAllCompanies()
                .forEach(companies -> {
                    dataSeries.add(new DataSeriesItem(companies.getName(), companies.getEmployeeCount()));
                });

        chart.getConfiguration().setSeries(dataSeries);

        return chart;
    }

    private Component getContactsStats() {
        Span stats = new Span(blogService.countContacts() + " contact");
        stats.addClassNames("text-xl", "mt-m");
        return stats;
    }
}
