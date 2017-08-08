import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Assurance } from './assurance.model';
import { AssuranceService } from './assurance.service';

@Component({
    selector: 'jhi-assurance-detail',
    templateUrl: './assurance-detail.component.html'
})
export class AssuranceDetailComponent implements OnInit, OnDestroy {

    assurance: Assurance;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private assuranceService: AssuranceService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInAssurances();
    }

    load(id) {
        this.assuranceService.find(id).subscribe((assurance) => {
            this.assurance = assurance;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInAssurances() {
        this.eventSubscriber = this.eventManager.subscribe(
            'assuranceListModification',
            (response) => this.load(this.assurance.id)
        );
    }
}
