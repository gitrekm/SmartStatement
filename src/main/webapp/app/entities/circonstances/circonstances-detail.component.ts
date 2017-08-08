import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Circonstances } from './circonstances.model';
import { CirconstancesService } from './circonstances.service';

@Component({
    selector: 'jhi-circonstances-detail',
    templateUrl: './circonstances-detail.component.html'
})
export class CirconstancesDetailComponent implements OnInit, OnDestroy {

    circonstances: Circonstances;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private circonstancesService: CirconstancesService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInCirconstances();
    }

    load(id) {
        this.circonstancesService.find(id).subscribe((circonstances) => {
            this.circonstances = circonstances;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInCirconstances() {
        this.eventSubscriber = this.eventManager.subscribe(
            'circonstancesListModification',
            (response) => this.load(this.circonstances.id)
        );
    }
}
