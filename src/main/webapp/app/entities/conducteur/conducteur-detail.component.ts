import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Conducteur } from './conducteur.model';
import { ConducteurService } from './conducteur.service';

@Component({
    selector: 'jhi-conducteur-detail',
    templateUrl: './conducteur-detail.component.html'
})
export class ConducteurDetailComponent implements OnInit, OnDestroy {

    conducteur: Conducteur;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private conducteurService: ConducteurService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInConducteurs();
    }

    load(id) {
        this.conducteurService.find(id).subscribe((conducteur) => {
            this.conducteur = conducteur;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInConducteurs() {
        this.eventSubscriber = this.eventManager.subscribe(
            'conducteurListModification',
            (response) => this.load(this.conducteur.id)
        );
    }
}
