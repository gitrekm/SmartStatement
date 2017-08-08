import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Vehicule } from './vehicule.model';
import { VehiculePopupService } from './vehicule-popup.service';
import { VehiculeService } from './vehicule.service';
import { Conducteur, ConducteurService } from '../conducteur';
import { Assuree, AssureeService } from '../assuree';
import { Constat, ConstatService } from '../constat';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-vehicule-dialog',
    templateUrl: './vehicule-dialog.component.html'
})
export class VehiculeDialogComponent implements OnInit {

    vehicule: Vehicule;
    isSaving: boolean;

    conducteurs: Conducteur[];

    assurees: Assuree[];

    constats: Constat[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private vehiculeService: VehiculeService,
        private conducteurService: ConducteurService,
        private assureeService: AssureeService,
        private constatService: ConstatService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.conducteurService.query()
            .subscribe((res: ResponseWrapper) => { this.conducteurs = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.assureeService.query()
            .subscribe((res: ResponseWrapper) => { this.assurees = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.constatService.query()
            .subscribe((res: ResponseWrapper) => { this.constats = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.vehicule.id !== undefined) {
            this.subscribeToSaveResponse(
                this.vehiculeService.update(this.vehicule));
        } else {
            this.subscribeToSaveResponse(
                this.vehiculeService.create(this.vehicule));
        }
    }

    private subscribeToSaveResponse(result: Observable<Vehicule>) {
        result.subscribe((res: Vehicule) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Vehicule) {
        this.eventManager.broadcast({ name: 'vehiculeListModification', content: 'OK'});
        this.isSaving = false;
        this.activeModal.dismiss(result);
    }

    private onSaveError(error) {
        try {
            error.json();
        } catch (exception) {
            error.message = error.text();
        }
        this.isSaving = false;
        this.onError(error);
    }

    private onError(error) {
        this.alertService.error(error.message, null, null);
    }

    trackConducteurById(index: number, item: Conducteur) {
        return item.id;
    }

    trackAssureeById(index: number, item: Assuree) {
        return item.id;
    }

    trackConstatById(index: number, item: Constat) {
        return item.id;
    }

    getSelected(selectedVals: Array<any>, option: any) {
        if (selectedVals) {
            for (let i = 0; i < selectedVals.length; i++) {
                if (option.id === selectedVals[i].id) {
                    return selectedVals[i];
                }
            }
        }
        return option;
    }
}

@Component({
    selector: 'jhi-vehicule-popup',
    template: ''
})
export class VehiculePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private vehiculePopupService: VehiculePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.vehiculePopupService
                    .open(VehiculeDialogComponent as Component, params['id']);
            } else {
                this.vehiculePopupService
                    .open(VehiculeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
