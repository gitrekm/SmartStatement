import { BaseEntity } from './../../shared';

export class Circonstances implements BaseEntity {
    constructor(
        public id?: number,
        public circonstance?: string,
        public description?: string,
        public constat?: BaseEntity,
    ) {
    }
}
