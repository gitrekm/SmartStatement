import { BaseEntity } from './../../shared';

const enum YesNo {
    'YES',
    'NO'
}

export class Constat implements BaseEntity {
    constructor(
        public id?: number,
        public dateConstat?: any,
        public lieuConstat?: string,
        public temoins?: string,
        public degats?: YesNo,
        public degatsApparent?: YesNo,
        public blesses?: YesNo,
        public pointDeChocInitialA?: string,
        public observationsA?: string,
        public pointDeChocInitialB?: string,
        public observationsB?: string,
        public sensSuiviA?: string,
        public venantDeA?: string,
        public allantAA?: string,
        public sensSuiviB?: string,
        public venantDeB?: string,
        public allantAB?: string,
        public circonstances?: BaseEntity,
        public conducteurAS?: BaseEntity[],
        public conducteurBS?: BaseEntity[],
        public vehiculeAS?: BaseEntity[],
        public vehiculeBS?: BaseEntity[],
        public assuranceAS?: BaseEntity[],
        public assuranceBS?: BaseEntity[],
        public assureeAS?: BaseEntity[],
        public assureeBS?: BaseEntity[],
    ) {
    }
}
