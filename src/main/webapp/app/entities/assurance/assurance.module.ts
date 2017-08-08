import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartStatementSharedModule } from '../../shared';
import {
    AssuranceService,
    AssurancePopupService,
    AssuranceComponent,
    AssuranceDetailComponent,
    AssuranceDialogComponent,
    AssurancePopupComponent,
    AssuranceDeletePopupComponent,
    AssuranceDeleteDialogComponent,
    assuranceRoute,
    assurancePopupRoute,
} from './';

const ENTITY_STATES = [
    ...assuranceRoute,
    ...assurancePopupRoute,
];

@NgModule({
    imports: [
        SmartStatementSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AssuranceComponent,
        AssuranceDetailComponent,
        AssuranceDialogComponent,
        AssuranceDeleteDialogComponent,
        AssurancePopupComponent,
        AssuranceDeletePopupComponent,
    ],
    entryComponents: [
        AssuranceComponent,
        AssuranceDialogComponent,
        AssurancePopupComponent,
        AssuranceDeleteDialogComponent,
        AssuranceDeletePopupComponent,
    ],
    providers: [
        AssuranceService,
        AssurancePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartStatementAssuranceModule {}
