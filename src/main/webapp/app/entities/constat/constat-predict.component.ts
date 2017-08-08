import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Constat } from './constat.model';
import { ConstatService } from './constat.service';
import { ConstatPredict } from './constatpredict.model';
import { ConstatPredictService } from './constatpredict.service';
@Component({
    selector: 'jhi-constat-detail',
    templateUrl: './constat-predict.component.html'
})

export class ConstatPredictComponent implements OnInit, OnDestroy {

    constatpredict: ConstatPredict;
    private subscription: Subscription;
    private eventSubscriber: Subscription;


    constructor(
        private eventManager: JhiEventManager,
        private constatService: ConstatService,
        private ConstatPredictService: ConstatPredictService,
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
            this.ConstatPredictService.find(id).subscribe((constatpredict) => {
                this.constatpredict = constatpredict;
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
            (response) => this.load(this.constatpredict.id)
        );
    }
}
