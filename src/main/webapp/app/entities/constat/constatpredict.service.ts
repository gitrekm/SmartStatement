import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils } from 'ng-jhipster';
import { ConstatPredict } from './constatpredict.model';
import { Constat } from './constat.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class ConstatPredictService {

    private resourceUrl = 'http://92.222.74.159:3000/prediction';
    private resourceSearchUrl = 'api/_';

    constructor(private http: Http, private dateUtils: JhiDateUtils) { }


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
