import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Assuree } from './assuree.model';
import { AssureePopupService } from './assuree-popup.service';
import { AssureeService } from './assuree.service';
import { Vehicule, VehiculeService } from '../vehicule';
import { Constat, ConstatService } from '../constat';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-assuree-dialog',
    templateUrl: './assuree-dialog.component.html'
})
export class AssureeDialogComponent implements OnInit {

    assuree: Assuree;
    isSaving: boolean;

    vehicules: Vehicule[];

    vehicleconducteurs: Vehicule[];

    constats: Constat[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private assureeService: AssureeService,
        private vehiculeService: VehiculeService,
        private constatService: ConstatService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.vehiculeService
            .query({filter: 'assuree-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.assuree.vehicule || !this.assuree.vehicule.id) {
                    this.vehicules = res.json;
                } else {
                    this.vehiculeService
                        .find(this.assuree.vehicule.id)
                        .subscribe((subRes: Vehicule) => {
                            this.vehicules = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.vehiculeService
            .query({filter: 'conducteur-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.assuree.vehicleConducteur || !this.assuree.vehicleConducteur.id) {
                    this.vehicleconducteurs = res.json;
                } else {
                    this.vehiculeService
                        .find(this.assuree.vehicleConducteur.id)
                        .subscribe((subRes: Vehicule) => {
                            this.vehicleconducteurs = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.constatService.query()
            .subscribe((res: ResponseWrapper) => { this.constats = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.assuree.id !== undefined) {
            this.subscribeToSaveResponse(
                this.assureeService.update(this.assuree));
        } else {
            this.subscribeToSaveResponse(
                this.assureeService.create(this.assuree));
        }
    }

    private subscribeToSaveResponse(result: Observable<Assuree>) {
        result.subscribe((res: Assuree) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Assuree) {
        this.eventManager.broadcast({ name: 'assureeListModification', content: 'OK'});
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

    trackVehiculeById(index: number, item: Vehicule) {
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
    selector: 'jhi-assuree-popup',
    template: ''
})
export class AssureePopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private assureePopupService: AssureePopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.assureePopupService
                    .open(AssureeDialogComponent as Component, params['id']);
            } else {
                this.assureePopupService
                    .open(AssureeDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
