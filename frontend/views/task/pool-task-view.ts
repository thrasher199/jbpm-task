import { ProcessEndpoint } from 'Frontend/generated/endpoints';
import { VirtualListLitRenderer, virtualListRenderer } from '@vaadin/virtual-list/lit.js';
import TaskSummaryDto from 'Frontend/generated/com/jbpmtask/application/data/dto/TaskSummaryDto';
import { html } from 'lit';
import { customElement, property, state } from 'lit/decorators.js';
import { View } from '../view';
import '@vaadin/vertical-layout';
import '@vaadin/virtual-list';

@customElement('pool-task-view')
export class PoolTaskView extends View {
  @state()
  private taskSummaryDto: TaskSummaryDto[] = [];

  render() {
    return html`
        <vaadin-virtual-list
          .items="${this.taskSummaryDto}"
          ${virtualListRenderer(this.taskSummaryVLRenderer, [])}
        ></vaadin-virtual-list>
    `;
  }

  async connectedCallback() {
    super.connectedCallback();
    this.taskSummaryDto = await ProcessEndpoint.poolList();
    //ProcessEndpoint.getFlux().onNext(task => this.taskSummaryDto = [...this.taskSummaryDto, task]);
    this.classList.add('flex', 'flex-col', 'h-full', 'p-l', 'box-border');
  }

  private taskSummaryVLRenderer: VirtualListLitRenderer<TaskSummaryDto> = (taskSummary) => {
    return html`
      <vaadin-vertical-layout style="border-style: solid;">
        <h3>Name: ${taskSummary.name}</h3>
        <h4>Id :${taskSummary.id}</h4>
        <vaadin-button @click="${() => this.claimTask(taskSummary.id)}">
                    <vaadin-icon icon="vaadin:start-cog"></vaadin-icon>
                    Claim
                </vaadin-button>
      </vaadin-vertical-layout>
    `;
  };

  async claimTask(taskId : number) {
    this.taskSummaryDto = await ProcessEndpoint.claimTask(taskId);
  }
}
