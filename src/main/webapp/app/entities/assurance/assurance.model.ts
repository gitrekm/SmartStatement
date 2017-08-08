import { BaseEntity } from './../../shared';

export class Assurance implements BaseEntity {
    constructor(
        public id?: number,
        public nomAssurance?: string,
        public numAssurnance?: number,
        public agence?: string,
        public valableAu?: any,
        public constatPartAS?: BaseEntity[],
        public constatPartBS?: BaseEntity[],
    ) {
    }
}
