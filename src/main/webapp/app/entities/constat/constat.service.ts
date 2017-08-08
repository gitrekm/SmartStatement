import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';

import { Constat } from './constat.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ConstatService {

    private resourceUrl = 'api/constats';
    private resourceSearchUrl = 'api/_search/constats';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }

    create(constat: Constat): Observable<Constat> {
        const copy = this.convert(constat);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    update(constat: Constat): Observable<Constat> {
        const copy = this.convert(constat);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            this.convertItemFromServer(jsonResponse);
            return jsonResponse;
        });
    }

    find(id: number): Observable<Constat> {
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
        entity.dateConstat = this.dateUtils
            .convertDateTimeFromServer(entity.dateConstat);
    }

    private convert(constat: Constat): Constat {
        const copy: Constat = Object.assign({}, constat);

        copy.dateConstat = this.dateUtils.toDate(constat.dateConstat);
        return copy;
    }
}
