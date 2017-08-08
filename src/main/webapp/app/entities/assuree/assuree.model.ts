import { BaseEntity } from './../../shared';

export class Assuree implements BaseEntity {
    constructor(
        public id?: number,
        public nomAssuree?: string,
        public prenomsAssuree?: string,
        public addresse?: string,
        public numTel?: string,
        public vehicule?: BaseEntity,
        public vehicleConducteur?: BaseEntity,
        public constatPartAS?: BaseEntity[],
        public constatPartBS?: BaseEntity[],
    ) {
    }
}
