import { Binder, field } from "@hilla/form";
import { VirtualListLitRenderer, virtualListRenderer } from "@vaadin/virtual-list/lit";
import Group from "Frontend/generated/com/jbpmtask/application/data/entity/Group";
import GroupModel from "Frontend/generated/com/jbpmtask/application/data/entity/GroupModel";
import { GroupEndpoint } from "Frontend/generated/endpoints";
import { html } from "lit";
import { customElement, state } from "lit/decorators";
import { View } from "../view";
import '@vaadin/text-field';
import '@vaadin/button';
import '@vaadin/horizontal-layout';
import '@vaadin/icon';
import '@vaadin/virtual-list';

@customElement('group-detail-view')
export class GroupDetailView extends View {
    @state()
    private group: Group[] = [];

    private binder = new Binder(this, GroupModel);

    render() {
        const { model } = this.binder;

        return html`
        <div>
            <vaadin-text-field placeholder="Add New Group" ${field(model.name)}></vaadin-text-field>
            <vaadin-button @click="${this.addGroup}">Add</vaadin-button>
        </div>
        <div>
            <vaadin-virtual-list  .items="${this.group}" ${virtualListRenderer(this.groupVLRenderer, [])}>
            </vaadin-virtual-list>
        </div>   
        `;
    }

    async connectedCallback() {
        super.connectedCallback();
        this.group = await GroupEndpoint.list();
        this.classList.add(
            'flex',
            'flex-col',
            'h-full',
            'p-l',
            'box-border'
        );
    }

    async addGroup() {
        const saved = await this.binder.submitTo(GroupEndpoint.save);
        if (saved) {
            this.group = [...this.group, saved];
            this.binder.clear;
        }
    }

    async removeGroup(deleted: Group) {
        await GroupEndpoint.delete(deleted.id);
        this.group = this.group.filter(g => g.id !== deleted.id);
    }

    private groupVLRenderer: VirtualListLitRenderer<Group> = (group) => {
        return html`
          <vaadin-horizontal-layout theme="spacing">
            <vaadin-button aria-label="Remove" @click="${() => this.removeGroup(group)}">
              <vaadin-icon icon="vaadin:minus-circle"></vaadin-icon>
            </vaadin-button>
            <b>${group.name}</b>
          </vaadin-horizontal-layout>
        `;
    }
}