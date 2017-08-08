import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Conducteur } from './conducteur.model';
import { ConducteurPopupService } from './conducteur-popup.service';
import { ConducteurService } from './conducteur.service';
import { Constat, ConstatService } from '../constat';
import { Vehicule, VehiculeService } from '../vehicule';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-conducteur-dialog',
    templateUrl: './conducteur-dialog.component.html'
})
export class ConducteurDialogComponent implements OnInit {

    conducteur: Conducteur;
    isSaving: boolean;

    constats: Constat[];

    vehicules: Vehicule[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private conducteurService: ConducteurService,
        private constatService: ConstatService,
        private vehiculeService: VehiculeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.constatService.query()
            .subscribe((res: ResponseWrapper) => { this.constats = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.vehiculeService.query()
            .subscribe((res: ResponseWrapper) => { this.vehicules = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.conducteur.id !== undefined) {
            this.subscribeToSaveResponse(
                this.conducteurService.update(this.conducteur));
        } else {
            this.subscribeToSaveResponse(
                this.conducteurService.create(this.conducteur));
        }
    }

    private subscribeToSaveResponse(result: Observable<Conducteur>) {
        result.subscribe((res: Conducteur) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Conducteur) {
        this.eventManager.broadcast({ name: 'conducteurListModification', content: 'OK'});
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

    trackConstatById(index: number, item: Constat) {
        return item.id;
    }

    trackVehiculeById(index: number, item: Vehicule) {
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
    selector: 'jhi-conducteur-popup',
    template: ''
})
export class ConducteurPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private conducteurPopupService: ConducteurPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.conducteurPopupService
                    .open(ConducteurDialogComponent as Component, params['id']);
            } else {
                this.conducteurPopupService
                    .open(ConducteurDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
