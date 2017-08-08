import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Constat } from './constat.model';
import { ConstatService } from './constat.service';

@Component({
    selector: 'jhi-constat-detail',
    templateUrl: './constat-detail.component.html'
})
export class ConstatDetailComponent implements OnInit, OnDestroy {

    constat: Constat;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private constatService: ConstatService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInConstats();
    }

    load(id) {
        this.constatService.find(id).subscribe((constat) => {
            this.constat = constat;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInConstats() {
        this.eventSubscriber = this.eventManager.subscribe(
            'constatListModification',
            (response) => this.load(this.constat.id)
        );
    }
}
