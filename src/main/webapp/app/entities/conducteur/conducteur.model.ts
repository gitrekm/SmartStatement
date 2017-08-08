import { BaseEntity } from './../../shared';

export class Conducteur implements BaseEntity {
    constructor(
        public id?: number,
        public nom?: string,
        public prenom?: string,
        public email?: string,
        public tel?: string,
        public permisNum?: number,
        public livreeLe?: any,
        public addresse?: string,
        public constatAS?: BaseEntity[],
        public constatBS?: BaseEntity[],
        public vehicules?: BaseEntity[],
    ) {
    }
}
