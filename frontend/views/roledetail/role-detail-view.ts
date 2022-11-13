import { VirtualListLitRenderer, virtualListRenderer } from '@vaadin/virtual-list/lit.js';
import { Binder, field } from '@hilla/form';
import { View } from '../view';
import { customElement, state } from 'lit/decorators.js';
import { html } from 'lit';
import '@vaadin/text-field';
import '@vaadin/button';
import RoleModel from 'Frontend/generated/com/jbpmtask/application/data/entity/RoleModel';
import '@vaadin/horizontal-layout';
import '@vaadin/icon';
import '@vaadin/virtual-list';
import Role from 'Frontend/generated/com/jbpmtask/application/data/entity/Role';
import { RoleEndpoint } from 'Frontend/generated/endpoints';

@customElement('role-detail-view')
export class RoleDetailView extends View {
  @state()
  private role: Role[] = [];

  private binder = new Binder(this, RoleModel);

  render() {
    const { model } = this.binder;

    return html`
      <div>
        <vaadin-text-field placeholder="Add New Role" ${field(model.name)}></vaadin-text-field>
        <vaadin-button @click="${this.addRole}">Add</vaadin-button>
      </div>
      <div>
        <vaadin-virtual-list  .items="${this.role}" ${virtualListRenderer(this.roleVLRenderer, [])}>
        </vaadin-virtual-list>
      </div>
    `;
  }

  async connectedCallback() {
    super.connectedCallback();
    this.role = await RoleEndpoint.list();
    this.classList.add(
        'flex',
        'flex-col',
        'h-full',
        'p-l',
        'box-border'
    );
  }

  async addRole(){
      const saved = await this.binder.submitTo(RoleEndpoint.save);
      if(saved){
        this.role = [...this.role, saved];
        this.binder.clear;
      }
  }

  private roleVLRenderer: VirtualListLitRenderer<Role> = (role) => {
    return html`
      <vaadin-horizontal-layout theme="spacing">
        <vaadin-button aria-label="Remove" @click="${() => this.removeRole(role)}">
          <vaadin-icon icon="vaadin:minus-circle"></vaadin-icon>
        </vaadin-button>
        <b>${role.name}</b>
      </vaadin-horizontal-layout>
    `;
  }

  async removeRole(deleted: Role){ 
    await RoleEndpoint.delete(deleted.id);
    this.role = this.role.filter(r => r.id !== deleted.id);
  }
}
