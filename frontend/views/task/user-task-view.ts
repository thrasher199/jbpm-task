import { ProcessEndpoint } from 'Frontend/generated/endpoints';
import { VirtualListLitRenderer, virtualListRenderer } from '@vaadin/virtual-list/lit.js';
import TaskSummaryDto from 'Frontend/generated/com/jbpmtask/application/data/dto/TaskSummaryDto';
import { html } from 'lit';
import { customElement, state } from 'lit/decorators.js';
import { View } from '../view';
import '@vaadin/vertical-layout';
import '@vaadin/virtual-list';

@customElement('user-task-view')
export class UserTaskView extends View {
  @state()
  taskSummaryDto: TaskSummaryDto[] = [];

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
    //this.taskSummaryDto = await ProcessEndpoint.listUserTask();
    ProcessEndpoint.getFlux().onNext(task => this.taskSummaryDto = [...this.taskSummaryDto, task]);
    this.classList.add('flex', 'flex-col', 'h-full', 'p-l', 'box-border');
  }

  private taskSummaryVLRenderer: VirtualListLitRenderer<TaskSummaryDto> = (taskSummary) => {
    return html`
      <vaadin-vertical-layout style="border-style: solid;">
        <h3>Name: ${taskSummary.name}</h3>
        <h4>Id :${taskSummary.id}</h4>
        <!-- <h4>${taskSummary.processId}</h4>
        <h4>${taskSummary.processInstanceId}</h4> -->
      </vaadin-vertical-layout>
    `;
  };
}
