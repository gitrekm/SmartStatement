import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';
import { SmartStatementSharedModule } from '../../shared';
import {
    ConstatService,
    ConstatPopupService,
    ConstatComponent,
    ConstatDetailComponent,
    ConstatPredictComponent,
    ConstatPredictService,
    ConstatDialogComponent,
    ConstatPopupComponent,
    ConstatDeletePopupComponent,
    ConstatDeleteDialogComponent,
    constatRoute,
    constatPopupRoute,
} from './';

const ENTITY_STATES = [
    ...constatRoute,
    ...constatPopupRoute,
];

@NgModule({
    imports: [
        SmartStatementSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        ConstatComponent,
        ConstatDetailComponent,
    ConstatPredictComponent,
        ConstatDialogComponent,
        ConstatDeleteDialogComponent,
        ConstatPopupComponent,
        ConstatDeletePopupComponent,
    ],
    entryComponents: [
        ConstatComponent,
        ConstatPredictComponent,
        ConstatDialogComponent,
        ConstatPopupComponent,
        ConstatDeleteDialogComponent,
        ConstatDeletePopupComponent,
    ],
    providers: [
        ConstatService,
        ConstatPopupService,
        ConstatPredictService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartStatementConstatModule {}
