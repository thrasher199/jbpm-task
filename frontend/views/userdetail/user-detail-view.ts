import { customElement, property, query, state } from 'lit/decorators.js';
import { Binder, field } from '@hilla/form';
import { EndpointError } from '@hilla/frontend';
import { Grid, GridDataProviderParams, GridDataProviderCallback } from '@vaadin/grid';
import { columnBodyRenderer, GridColumnBodyLitRenderer } from '@vaadin/grid/lit';
import User from 'Frontend/generated/com/jbpmtask/application/data/entity/User';
import UserModel from 'Frontend/generated/com/jbpmtask/application/data/entity/UserModel';
import Sort from 'Frontend/generated/dev/hilla/mappedtypes/Sort';
import { UserCrudEndpoint } from 'Frontend/generated/endpoints';
import Direction from 'Frontend/generated/org/springframework/data/domain/Sort/Direction';
import { html } from 'lit';
import { View } from '../view';
import '@vaadin/button';
import '@vaadin/date-picker';
import '@vaadin/date-time-picker';
import '@vaadin/form-layout';
import '@vaadin/grid';
import '@vaadin/grid/vaadin-grid-sort-column';
import '@vaadin/horizontal-layout';
import '@vaadin/icon';
import '@vaadin/icons';
import '@vaadin/notification';
import '@vaadin/polymer-legacy-adapter';
import '@vaadin/split-layout';
import '@vaadin/text-field';
import '@vaadin/upload';
import '@vaadin/vaadin-icons';
import { Notification } from '@vaadin/notification';
import '@vaadin/password-field';
import '@vaadin/avatar';
import '@vaadin/checkbox-group';
import '@vaadin/checkbox';
import '@vaadin/list-box';
import '@vaadin/item';
import '@vaadin/multi-select-combo-box';
import Role from 'Frontend/generated/com/jbpmtask/application/data/entity/Role';
import '@vaadin/combo-box';
import '@vaadin/virtual-list';
import '@vaadin/text-area';

import { ComboBox } from '@vaadin/combo-box';
import { MultiSelectComboBoxSelectedItemsChangedEvent } from '@vaadin/multi-select-combo-box';

@customElement('user-detail-view')
export class UserDetailView extends View {
  @query('#grid')
  private grid!: Grid;

  @query('#roleComboBox')
  private roleComboBox!: ComboBox;

  @property({ type: Number })
  private gridSize = 0;

  private gridDataProvider = this.getGridData.bind(this);

  private binder = new Binder(this, UserModel);

  @state()
  private roles: Role[] = [];

  @state()
  private rolesDG: Role[] = [];

  @state()
  private selecetedRole: Role[] = [];

  render() {
    return html`
      <vaadin-split-layout>
        <div class="grid-wrapper">
          <vaadin-grid
            id="grid"
            theme="no-border"
            .size=${this.gridSize}
            .dataProvider=${this.gridDataProvider}
            @active-item-changed=${this.itemSelected}
          >
            <vaadin-grid-column
              header="Profile Picture"
              auto-width
              ${columnBodyRenderer<User>((user) => html` <vaadin-avatar name="${user.name}"> </vaadin-avatar> `)}
            >
            </vaadin-grid-column>
            <vaadin-grid-sort-column path="name" auto-width></vaadin-grid-sort-column>
            <vaadin-grid-sort-column path="username" auto-width></vaadin-grid-sort-column>
            <vaadin-grid-sort-column ${columnBodyRenderer(this.rolesRenderer, [])}> </vaadin-grid-sort-column>
          </vaadin-grid>
        </div>
        <div class="editor-layout">
          <div class="editor">
            <vaadin-form-layout>
              <vaadin-text-field label="Name" id="name" ${field(this.binder.model.name)}></vaadin-text-field>
              <vaadin-text-field
                label="Username"
                id="username"
                ${field(this.binder.model.username)}
              ></vaadin-text-field>
              <vaadin-password-field
                label="Password"
                ${field(this.binder.model.hashedPassword)}
              ></vaadin-password-field>
              <vaadin-vertical-layout theme="spacing">
                  <vaadin-multi-select-combo-box
                    label="Roles"
                    item-label-path="name"
                    item-id-path="id"
                    .items="${this.roles}"
                    ${field(this.binder.model.roles)}>
                  </vaadin-multi-select-combo-box>

                  <vaadin-text-area
                    label="Selected Roles"
                    readonly
                    .value="${this.selectedRolesText}"
                  ></vaadin-text-area>
                  </vaadin-form-layout>
              </vaadin-vertical-layout>
        </div>
      </vaadin-horizontal-layout>
          <vaadin-horizontal-layout class="button-layout">
            <vaadin-button theme="primary" @click=${this.save}>Save</vaadin-button>
            <vaadin-button theme="tertiary" @click=${this.cancel}>Cancel</vaadin-button>
            <vaadin-button theme="error" @click=${this.delete}>Delete</vaadin-button>
          </vaadin-horizontal-layout>
        </div>
      </vaadin-split-layout>

     
     
    `;
  }

  private get selectedRolesText(): string {
    return this.selecetedRole.map((role) => role.name).join(', ');
  }

  private async getGridData(
    params: GridDataProviderParams<User>,
    callback: GridDataProviderCallback<User | undefined>
  ) {
    const sort: Sort = {
      orders: params.sortOrders.map((order) => ({
        property: order.path,
        direction: order.direction == 'asc' ? Direction.ASC : Direction.DESC,
        ignoreCase: false,
      })),
    };
    const data = await UserCrudEndpoint.list({ pageNumber: params.page, pageSize: params.pageSize, sort });
    callback(data);
  }

  async connectedCallback() {
    super.connectedCallback();
    this.gridSize = (await UserCrudEndpoint.count()) ?? 0;
    this.roles = await UserCrudEndpoint.roleList();
  }

  private async itemSelected(event: CustomEvent) {
    const item: User = event.detail.value as User;
    this.grid.selectedItems = item ? [item] : [];

    if (item) {
      const fromBackend = await UserCrudEndpoint.get(item.id!);
      fromBackend ? this.binder.read(fromBackend) : this.refreshGrid();
      this.rolesDG = this.binder.value.roles as Role[];
      console.log(this.rolesDG);
    } else {
      this.clearForm();
    }
  }

  private async save() {
    try {
      const isNew = !this.binder.value.id;
      await this.binder.submitTo(UserCrudEndpoint.update);
      if (isNew) {
        // We added a new item
        this.gridSize++;
      }
      this.clearForm();
      this.refreshGrid();

      Notification.show(`User details stored.`, { position: 'bottom-start' });
    } catch (error: any) {
      if (error instanceof EndpointError) {
        Notification.show(`Server error. ${error.message}`, { theme: 'error', position: 'bottom-start' });
      } else {
        throw error;
      }
    }
  }

  private cancel() {
    this.grid.activeItem = undefined;
    this.rolesDG = [];
  }

  private async delete() {

  }

  private clearForm() {
    this.binder.clear();
  }

  private refreshGrid() {
    this.grid.selectedItems = [];
    this.grid.clearCache();
  }

  private rolesRenderer: GridColumnBodyLitRenderer<User> = (user) => {
    return html`
      <vaadin-horizontal-layout theme="spacing">
        ${user.roles?.map((role) => html` <span theme="badge">${role?.name}</span> `)}
      </vaadin-horizontal-layout>
    `;
  };

  private addRole() {
    const item = this.roleComboBox.selectedItem;
    if (item) {
      this.binder.value.roles = [];
      this.binder.for(this.binder.model.roles).appendItem(item);
      this.rolesDG = this.binder.value.roles as Role[];
    }
  }
}
