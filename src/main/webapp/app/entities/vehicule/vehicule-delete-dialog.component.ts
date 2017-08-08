import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { Vehicule } from './vehicule.model';
import { VehiculePopupService } from './vehicule-popup.service';
import { VehiculeService } from './vehicule.service';

@Component({
    selector: 'jhi-vehicule-delete-dialog',
    templateUrl: './vehicule-delete-dialog.component.html'
})
export class VehiculeDeleteDialogComponent {

    vehicule: Vehicule;

    constructor(
        private vehiculeService: VehiculeService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.vehiculeService.delete(id).subscribe((response) => {
            this.eventManager.broadcast({
                name: 'vehiculeListModification',
                content: 'Deleted an vehicule'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-vehicule-delete-popup',
    template: ''
})
export class VehiculeDeletePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private vehiculePopupService: VehiculePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            this.vehiculePopupService
                .open(VehiculeDeleteDialogComponent as Component, params['id']);
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
