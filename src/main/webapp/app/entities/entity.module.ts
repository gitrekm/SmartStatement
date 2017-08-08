import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { SmartStatementConstatModule } from './constat/constat.module';
import { SmartStatementVehiculeModule } from './vehicule/vehicule.module';
import { SmartStatementConducteurModule } from './conducteur/conducteur.module';
import { SmartStatementAssuranceModule } from './assurance/assurance.module';
import { SmartStatementAssureeModule } from './assuree/assuree.module';
import { SmartStatementCirconstancesModule } from './circonstances/circonstances.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    imports: [
        SmartStatementConstatModule,
        SmartStatementVehiculeModule,
        SmartStatementConducteurModule,
        SmartStatementAssuranceModule,
        SmartStatementAssureeModule,
        SmartStatementCirconstancesModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class SmartStatementEntityModule {}
