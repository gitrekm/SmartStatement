import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';

import { Circonstances } from './circonstances.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class CirconstancesService {

    private resourceUrl = 'api/circonstances';
    private resourceSearchUrl = 'api/_search/circonstances';

    constructor(private http: Http) { }

    create(circonstances: Circonstances): Observable<Circonstances> {
        const copy = this.convert(circonstances);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    update(circonstances: Circonstances): Observable<Circonstances> {
        const copy = this.convert(circonstances);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            return res.json();
        });
    }

    find(id: number): Observable<Circonstances> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            return res.json();
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
        return new ResponseWrapper(res.headers, jsonResponse, res.status);
    }

    private convert(circonstances: Circonstances): Circonstances {
        const copy: Circonstances = Object.assign({}, circonstances);
        return copy;
    }
}
