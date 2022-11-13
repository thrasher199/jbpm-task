import { VirtualListLitRenderer, virtualListRenderer } from '@vaadin/virtual-list/lit.js';
import { html } from 'lit';
import { customElement, property, state } from 'lit/decorators.js';
import { View } from './../view';
import ProcessDefDto from 'Frontend/generated/com/jbpmtask/application/data/dto/ProcessDefDto';
import '@vaadin/virtual-list';
import { ProcessEndpoint } from 'Frontend/generated/endpoints';
import '@vaadin/vertical-layout';
import '@vaadin/icon';
import '@vaadin/button';
import { Notification } from '@vaadin/notification';

@customElement('process-view')
export class ProcessView extends View {

    @state()
    private processDef: ProcessDefDto[] = []

    @property({ type: Number })
    private processInstanceId = 0;
    

    render(){ 
        return html`
           <div>
                <vaadin-virtual-list theme="spacing"
                .items="${this.processDef}"
                ${virtualListRenderer(this.processDefVLRenderer, [])}>

                </vaadin-virtual-list>
           </div>
        `;
    }

    async connectedCallback() {
        super.connectedCallback();
        this.processDef = await ProcessEndpoint.listProcess();
        this.classList.add(
            'flex',
            'flex-col',
            'h-full',
            'p-l',
            'box-border'
        );
    }

    private processDefVLRenderer: VirtualListLitRenderer<ProcessDefDto> = (processDef) => {
        return html`
            <vaadin-vertical-layout>
                <h3>${processDef.name}</h3>
                <h4>${processDef.deploymentId}</h4>
                <vaadin-button @click="${() => this.startProcess(processDef.deploymentId!, processDef.id!)}">
                    <vaadin-icon icon="vaadin:start-cog"></vaadin-icon>
                    Start
                </vaadin-button>
            </vaadin-vertical-layout>
        `;
    }

    async startProcess(deploymentId: string, processDefId : string) {
        this.processInstanceId = await ProcessEndpoint.startProcess(deploymentId, processDefId);
        if(processDefId){
            const notification = Notification.show(`Process started with process instance id : ${this.processInstanceId}`, { position: 'middle' });
            notification.setAttribute('theme', 'success');
        }
    }
}