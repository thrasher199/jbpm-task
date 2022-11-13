import '@vaadin-component-factory/vcf-nav';
import '@vaadin/app-layout';
import { AppLayout } from '@vaadin/app-layout';
import '@vaadin/app-layout/vaadin-drawer-toggle';
import '@vaadin/avatar/vaadin-avatar';
import '@vaadin/context-menu';
import '@vaadin/tabs';
import '@vaadin/tabs/vaadin-tab';
import { html, render } from 'lit';
import { customElement } from 'lit/decorators.js';
import { logout } from '../auth';
import { router } from '../index';
import { hasAccess, views } from '../routes';
import { appStore } from '../stores/app-store';
import { Layout } from './view';

interface RouteInfo {
  path: string;
  title: string;
  icon: string;
}

@customElement('main-layout')
export class MainLayout extends Layout {
  render() {
    return html`
      <vaadin-app-layout primary-section="drawer">
        <header class="view-header" slot="navbar">
          <vaadin-drawer-toggle aria-label="Menu toggle" class="view-toggle" theme="contrast"></vaadin-drawer-toggle>
          <h2 class="view-title">${appStore.currentViewTitle}</h2>
        </header>
        <section class="drawer-section" slot="drawer">
          <h1 class="app-name">${appStore.applicationName}</h1>
          <!-- vcf-nav is not yet an official component -->
          <!-- For documentation, visit https://github.com/vaadin/vcf-nav#readme -->
          <vcf-nav class="app-nav" aria-label="${appStore.applicationName}">
            ${this.getMenuRoutes().map(
              (viewRoute) => html`
                <vcf-nav-item path=${router.urlForPath(viewRoute.path)}>
                  <span class="${viewRoute.icon} nav-item-icon" slot="prefix" aria-hidden="true"></span>
                  ${viewRoute.title}
                </vcf-nav-item>
              `
            )}
          </vcf-nav>
          <footer class="app-nav-footer">
            ${appStore.user
              ? html` <vaadin-context-menu open-on="click" .renderer="${this.renderLogoutOptions}">
                  <vaadin-avatar
                    theme="xsmall"
                    name="${appStore.user.name}"
                  ></vaadin-avatar>
                  <span class="font-medium ms-xs text-s text-secondary">${appStore.user.name}</span>
                </vaadin-context-menu>`
              : html`<a router-ignore href="login">Sign in</a>`}
          </footer>
        </section>
        <slot></slot>
      </vaadin-app-layout>
    `;
  }

  connectedCallback() {
    super.connectedCallback();
    this.classList.add('block', 'h-full');
    this.reaction(
      () => appStore.location,
      () => {
        AppLayout.dispatchCloseOverlayDrawerEvent();
      }
    );
  }

  private renderLogoutOptions(root: HTMLElement) {
    render(html`<vaadin-list-box><vaadin-item @click=${() => logout()}>Logout</vaadin-item></vaadin-list-box>`, root);
  }

  private getMenuRoutes(): RouteInfo[] {
    return views.filter((route) => route.title).filter((route) => hasAccess(route)) as RouteInfo[];
  }
}
