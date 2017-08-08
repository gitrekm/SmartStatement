import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { Response } from '@angular/http';

import { Observable } from 'rxjs/Rx';
import { NgbActiveModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager, JhiAlertService } from 'ng-jhipster';

import { Constat } from './constat.model';
import { ConstatPopupService } from './constat-popup.service';
import { ConstatService } from './constat.service';
import { Circonstances, CirconstancesService } from '../circonstances';
import { Conducteur, ConducteurService } from '../conducteur';
import { Vehicule, VehiculeService } from '../vehicule';
import { Assurance, AssuranceService } from '../assurance';
import { Assuree, AssureeService } from '../assuree';
import { ResponseWrapper } from '../../shared';

@Component({
    selector: 'jhi-constat-dialog',
    templateUrl: './constat-dialog.component.html'
})
export class ConstatDialogComponent implements OnInit {

    constat: Constat;
    isSaving: boolean;

    circonstances: Circonstances[];

    conducteurs: Conducteur[];

    vehicules: Vehicule[];

    assurances: Assurance[];

    assurees: Assuree[];

    constructor(
        public activeModal: NgbActiveModal,
        private alertService: JhiAlertService,
        private constatService: ConstatService,
        private circonstancesService: CirconstancesService,
        private conducteurService: ConducteurService,
        private vehiculeService: VehiculeService,
        private assuranceService: AssuranceService,
        private assureeService: AssureeService,
        private eventManager: JhiEventManager
    ) {
    }

    ngOnInit() {
        this.isSaving = false;
        this.circonstancesService
            .query({filter: 'constat-is-null'})
            .subscribe((res: ResponseWrapper) => {
                if (!this.constat.circonstances || !this.constat.circonstances.id) {
                    this.circonstances = res.json;
                } else {
                    this.circonstancesService
                        .find(this.constat.circonstances.id)
                        .subscribe((subRes: Circonstances) => {
                            this.circonstances = [subRes].concat(res.json);
                        }, (subRes: ResponseWrapper) => this.onError(subRes.json));
                }
            }, (res: ResponseWrapper) => this.onError(res.json));
        this.conducteurService.query()
            .subscribe((res: ResponseWrapper) => { this.conducteurs = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.vehiculeService.query()
            .subscribe((res: ResponseWrapper) => { this.vehicules = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.assuranceService.query()
            .subscribe((res: ResponseWrapper) => { this.assurances = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
        this.assureeService.query()
            .subscribe((res: ResponseWrapper) => { this.assurees = res.json; }, (res: ResponseWrapper) => this.onError(res.json));
    }

    clear() {
        this.activeModal.dismiss('cancel');
    }

    save() {
        this.isSaving = true;
        if (this.constat.id !== undefined) {
            this.subscribeToSaveResponse(
                this.constatService.update(this.constat));
        } else {
            this.subscribeToSaveResponse(
                this.constatService.create(this.constat));
        }
    }

    private subscribeToSaveResponse(result: Observable<Constat>) {
        result.subscribe((res: Constat) =>
            this.onSaveSuccess(res), (res: Response) => this.onSaveError(res));
    }

    private onSaveSuccess(result: Constat) {
        this.eventManager.broadcast({ name: 'constatListModification', content: 'OK'});
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

    trackCirconstancesById(index: number, item: Circonstances) {
        return item.id;
    }

    trackConducteurById(index: number, item: Conducteur) {
        return item.id;
    }

    trackVehiculeById(index: number, item: Vehicule) {
        return item.id;
    }

    trackAssuranceById(index: number, item: Assurance) {
        return item.id;
    }

    trackAssureeById(index: number, item: Assuree) {
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
    selector: 'jhi-constat-popup',
    template: ''
})
export class ConstatPopupComponent implements OnInit, OnDestroy {

    routeSub: any;

    constructor(
        private route: ActivatedRoute,
        private constatPopupService: ConstatPopupService
    ) {}

    ngOnInit() {
        this.routeSub = this.route.params.subscribe((params) => {
            if ( params['id'] ) {
                this.constatPopupService
                    .open(ConstatDialogComponent as Component, params['id']);
            } else {
                this.constatPopupService
                    .open(ConstatDialogComponent as Component);
            }
        });
    }

    ngOnDestroy() {
        this.routeSub.unsubscribe();
    }
}
