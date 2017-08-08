import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartStatementSharedModule } from '../../shared';
import {
    CirconstancesService,
    CirconstancesPopupService,
    CirconstancesComponent,
    CirconstancesDetailComponent,
    CirconstancesDialogComponent,
    CirconstancesPopupComponent,
    CirconstancesDeletePopupComponent,
    CirconstancesDeleteDialogComponent,
    circonstancesRoute,
    circonstancesPopupRoute,
} from './';

const ENTITY_STATES = [
    ...circonstancesRoute,
    ...circonstancesPopupRoute,
];

@NgModule({
    imports: [
        SmartStatementSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        CirconstancesComponent,
        CirconstancesDetailComponent,
        CirconstancesDialogComponent,
        CirconstancesDeleteDialogComponent,
        CirconstancesPopupComponent,
        CirconstancesDeletePopupComponent,
    ],
    entryComponents: [
        CirconstancesComponent,
        CirconstancesDialogComponent,
        CirconstancesPopupComponent,
        CirconstancesDeleteDialogComponent,
        CirconstancesDeletePopupComponent,
    ],
    providers: [
        CirconstancesService,
        CirconstancesPopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartStatementCirconstancesModule {}
