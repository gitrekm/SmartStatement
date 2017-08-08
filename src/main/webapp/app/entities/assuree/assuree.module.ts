import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartStatementSharedModule } from '../../shared';
import {
    AssureeService,
    AssureePopupService,
    AssureeComponent,
    AssureeDetailComponent,
    AssureeDialogComponent,
    AssureePopupComponent,
    AssureeDeletePopupComponent,
    AssureeDeleteDialogComponent,
    assureeRoute,
    assureePopupRoute,
} from './';

const ENTITY_STATES = [
    ...assureeRoute,
    ...assureePopupRoute,
];

@NgModule({
    imports: [
        SmartStatementSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        AssureeComponent,
        AssureeDetailComponent,
        AssureeDialogComponent,
        AssureeDeleteDialogComponent,
        AssureePopupComponent,
        AssureeDeletePopupComponent,
    ],
    entryComponents: [
        AssureeComponent,
        AssureeDialogComponent,
        AssureePopupComponent,
        AssureeDeleteDialogComponent,
        AssureeDeletePopupComponent,
    ],
    providers: [
        AssureeService,
        AssureePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartStatementAssureeModule {}
