import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartStatementSharedModule } from '../../shared';
import {
    ConducteurService,
    ConducteurPopupService,
    ConducteurComponent,
    ConducteurDetailComponent,
    ConducteurDialogComponent,
    ConducteurPopupComponent,
    ConducteurDeletePopupComponent,
    ConducteurDeleteDialogComponent,
    conducteurRoute,
    conducteurPopupRoute,
} from './';

const ENTITY_STATES = [
    ...conducteurRoute,
    ...conducteurPopupRoute,
];

@NgModule({
    imports: [
        SmartStatementSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ConducteurComponent,
        ConducteurDetailComponent,
        ConducteurDialogComponent,
        ConducteurDeleteDialogComponent,
        ConducteurPopupComponent,
        ConducteurDeletePopupComponent,
    ],
    entryComponents: [
        ConducteurComponent,
        ConducteurDialogComponent,
        ConducteurPopupComponent,
        ConducteurDeleteDialogComponent,
        ConducteurDeletePopupComponent,
    ],
    providers: [
        ConducteurService,
        ConducteurPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartStatementConducteurModule {}
