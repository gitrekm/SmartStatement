import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SmartStatementSharedModule } from '../../shared';
import {
    VehiculeService,
    VehiculePopupService,
    VehiculeComponent,
    VehiculeDetailComponent,
    VehiculeDialogComponent,
    VehiculePopupComponent,
    VehiculeDeletePopupComponent,
    VehiculeDeleteDialogComponent,
    vehiculeRoute,
    vehiculePopupRoute,
} from './';

const ENTITY_STATES = [
    ...vehiculeRoute,
    ...vehiculePopupRoute,
];

@NgModule({
    imports: [
        SmartStatementSharedModule,
        RouterModule.forRoot(ENTITY_STATES, { useHash: true })
    ],
    declarations: [
        VehiculeComponent,
        VehiculeDetailComponent,
        VehiculeDialogComponent,
        VehiculeDeleteDialogComponent,
        VehiculePopupComponent,
        VehiculeDeletePopupComponent,
    ],
    entryComponents: [
        VehiculeComponent,
        VehiculeDialogComponent,
        VehiculePopupComponent,
        VehiculeDeleteDialogComponent,
        VehiculeDeletePopupComponent,
    ],
    providers: [
        VehiculeService,
        VehiculePopupService,
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartStatementVehiculeModule {}
