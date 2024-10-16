package com.petrovdns.vaadin.UI.views.navbar;

import com.petrovdns.vaadin.UI.views.admin.MainView;
import com.petrovdns.vaadin.UI.StatsView;
import com.petrovdns.vaadin.UI.views.blog.BlogView;
import com.petrovdns.vaadin.UI.views.blog.OwnerBlogPostView;
import com.petrovdns.vaadin.config.security.SecurityService;
import com.petrovdns.vaadin.config.security.SecurityUtil;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.theme.lumo.LumoIcon;

/**
 * <p>Instagram: @petrovdns
 * <p>Telegram: +37379666011 | @ixyck
 */

public class MainLayout extends AppLayout {

    private final SecurityService securityService;
    private final SecurityUtil securityUtil;

    public MainLayout(SecurityService securityService, SecurityUtil securityUtil) {
        this.securityService = securityService;
        this.securityUtil = securityUtil;
        createHeader();
        createDrawer();
    }

    private void createDrawer() {

        String userNameProfile = securityService.getAuthenticatedUser().getUsername();

        H1 logo = new H1("Petrov | BLOG");
        logo.addClassNames("text-l", "m-m", "cursor-default", "logo-pc-version");

        H1 logoMobileVersion = new H1("BLOG");
        logoMobileVersion.addClassNames("text-l", "m-m", "cursor-default", "logo-mobile-version");

        H1 userName = new H1(userNameProfile);
        userName.addClassNames("text-l", "m-m", "cursor-default");

        Button logOut = new Button("Log out", new Icon(VaadinIcon.CLOSE_SMALL), e -> securityService.logout());
        logOut.addThemeVariants(ButtonVariant.LUMO_ERROR);
        logOut.addClassNames("cursor-pointer", "logout-button");

        Button logOutMobileVersion = new Button("", new Icon(VaadinIcon.CLOSE_SMALL), e -> securityService.logout());
        logOutMobileVersion.addThemeVariants(ButtonVariant.LUMO_ERROR);
        logOutMobileVersion.addClassNames("cursor-pointer", "logout-button-mobile");

        Div rightContainer = new Div(userName, logOut, logOutMobileVersion);
        rightContainer.addClassNames("right-container");

        DrawerToggle drawer = new DrawerToggle();
        drawer.addClassNames("cursor-pointer", "no-outline");

        HorizontalLayout header = new HorizontalLayout(drawer, logo, logoMobileVersion, rightContainer);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames("py-0", "px-m");

        getStyle().set("position", "fixed");
        getStyle().set("top", "0");
        getStyle().setHeight("100%");
        getStyle().setWidth("100%");

        addToNavbar(header);

    }

    private void createHeader() {
        SideNav nav = new SideNav();

        SideNavItem usersLink = new SideNavItem("Users List",
                MainView.class, VaadinIcon.GROUP.create());
        usersLink.setVisible(securityUtil.isUserAdmin());

        SideNavItem ownerBlogLink = new SideNavItem("My Blog",
                OwnerBlogPostView.class, VaadinIcon.PAPERPLANE.create());
        ownerBlogLink.setVisible(!securityUtil.isUserAdmin());

        SideNavItem blogLink = new SideNavItem("Blog",
                BlogView.class, VaadinIcon.GRID_BIG.create());

        SideNavItem dashboardLink = new SideNavItem("Stats",
                StatsView.class, VaadinIcon.PIE_CHART.create());

        usersLink.addClassNames("side-nav-custom");
        blogLink.addClassNames("side-nav-custom");
        dashboardLink.addClassNames("side-nav-custom");

        VerticalLayout header = new VerticalLayout();
        header.setDefaultHorizontalComponentAlignment(FlexComponent.Alignment.START);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);
        header.addClassNames("py-5", "px-xl");

        nav.addItem(blogLink, ownerBlogLink, usersLink, dashboardLink);
        header.add(nav);

        addToDrawer(header);
    }
}
