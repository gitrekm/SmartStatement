import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager, JhiParseLinks, JhiPaginationUtil, JhiLanguageService, JhiAlertService } from 'ng-jhipster';

import { Circonstances } from './circonstances.model';
import { CirconstancesService } from './circonstances.service';
import { ITEMS_PER_PAGE, Principal, ResponseWrapper } from '../../shared';
import { PaginationConfig } from '../../blocks/config/uib-pagination.config';

@Component({
    selector: 'jhi-circonstances',
    templateUrl: './circonstances.component.html'
})
export class CirconstancesComponent implements OnInit, OnDestroy {
circonstances: Circonstances[];
    currentAccount: any;
    eventSubscriber: Subscription;
    currentSearch: string;

    constructor(
        private circonstancesService: CirconstancesService,
        private alertService: JhiAlertService,
        private eventManager: JhiEventManager,
        private activatedRoute: ActivatedRoute,
        private principal: Principal
    ) {
        this.currentSearch = activatedRoute.snapshot.params['search'] ? activatedRoute.snapshot.params['search'] : '';
    }

    loadAll() {
        if (this.currentSearch) {
            this.circonstancesService.search({
                query: this.currentSearch,
                }).subscribe(
                    (res: ResponseWrapper) => this.circonstances = res.json,
                    (res: ResponseWrapper) => this.onError(res.json)
                );
            return;
       }
        this.circonstancesService.query().subscribe(
            (res: ResponseWrapper) => {
                this.circonstances = res.json;
                this.currentSearch = '';
            },
            (res: ResponseWrapper) => this.onError(res.json)
        );
    }

    search(query) {
        if (!query) {
            return this.clear();
        }
        this.currentSearch = query;
        this.loadAll();
    }

    clear() {
        this.currentSearch = '';
        this.loadAll();
    }
    ngOnInit() {
        this.loadAll();
        this.principal.identity().then((account) => {
            this.currentAccount = account;
        });
        this.registerChangeInCirconstances();
    }

    ngOnDestroy() {
        this.eventManager.destroy(this.eventSubscriber);
    }

    trackId(index: number, item: Circonstances) {
        return item.id;
    }
    registerChangeInCirconstances() {
        this.eventSubscriber = this.eventManager.subscribe('circonstancesListModification', (response) => this.loadAll());
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }
}
