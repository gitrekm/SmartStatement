import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Subscription } from 'rxjs/Rx';
import { JhiEventManager  } from 'ng-jhipster';

import { Vehicule } from './vehicule.model';
import { VehiculeService } from './vehicule.service';

@Component({
    selector: 'jhi-vehicule-detail',
    templateUrl: './vehicule-detail.component.html'
})
export class VehiculeDetailComponent implements OnInit, OnDestroy {

    vehicule: Vehicule;
    private subscription: Subscription;
    private eventSubscriber: Subscription;

    constructor(
        private eventManager: JhiEventManager,
        private vehiculeService: VehiculeService,
        private route: ActivatedRoute
    ) {
    }

    ngOnInit() {
        this.subscription = this.route.params.subscribe((params) => {
            this.load(params['id']);
        });
        this.registerChangeInVehicules();
    }

    load(id) {
        this.vehiculeService.find(id).subscribe((vehicule) => {
            this.vehicule = vehicule;
        });
    }
    previousState() {
        window.history.back();
    }

    ngOnDestroy() {
        this.subscription.unsubscribe();
        this.eventManager.destroy(this.eventSubscriber);
    }

    registerChangeInVehicules() {
        this.eventSubscriber = this.eventManager.subscribe(
            'vehiculeListModification',
            (response) => this.load(this.vehicule.id)
        );
    }
}
