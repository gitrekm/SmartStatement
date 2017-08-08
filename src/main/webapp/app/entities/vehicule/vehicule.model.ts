import { BaseEntity } from './../../shared';

export class Vehicule implements BaseEntity {
    constructor(
        public id?: number,
        public marque?: string,
        public type?: string,
        public immatriculation?: string,
        public noms?: BaseEntity[],
        public assuree?: BaseEntity,
        public conducteur?: BaseEntity,
        public constatPartAS?: BaseEntity[],
        public constatPartBS?: BaseEntity[],
    ) {
    }
}
