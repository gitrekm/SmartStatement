import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Conducteur } from './conducteur.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ConducteurService {

    private resourceUrl = 'api/conducteurs';
    private resourceSearchUrl = 'api/_search/conducteurs';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(conducteur: Conducteur): Observable<Conducteur> {
        const copy = this.convert(conducteur);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(conducteur: Conducteur): Observable<Conducteur> {
        const copy = this.convert(conducteur);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Conducteur> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }

    private convertResponse(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        for (let i = 0; i < jsonResponse.length; i++) {
            this.convertItemFromServer(jsonResponse[i]);
        }
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convertItemFromServer(entity: any) {
        entity.livreeLe = this.dateUtils
            .convertDateTimeFromServer(entity.livreeLe);
    }

    private convert(conducteur: Conducteur): Conducteur {
        const copy: Conducteur = Object.assign({}, conducteur);

        copy.livreeLe = this.dateUtils.toDate(conducteur.livreeLe);
        return copy;
    }
}
